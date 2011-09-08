require 'sinatra'
require 'haml'
require 'java'

$: << File.join(File.dirname(__FILE__),'lib')

require 'helpers/html_helper'
require 'helpers/java_helper'
require 'helpers/package_helper'

$: << File.join(File.dirname(__FILE__),'..')

java_import 'org.hibernate.cfg.Configuration'

Core = PackageHelper.new("net.flipback.xpca.core")

SF = Configuration.new.configure.buildSessionFactory

def init_db
    session =  SF.openSession
    session.beginTransaction
    # Create root group
    root = Core.make_instance("XGroup", "/")
    root_id = session.save(root)
    # Create groups into root
    group_1 = Core.make_instance("XGroup", "group_1")
    group_1.group = root
    session.save(group_1)

    group_2 = Core.make_instance("XGroup", "group_2")
    group_2.group = root
    session.save(group_2)

    #Create objects into group_1
    object_1 = Core.make_instance("XObject", "object_1")
    object_1.group = group_1
    session.save(object_1)

    point_1 = Core.make_instance("XPoint", "point_1")
    point_1.group = group_1
    session.save(point_1)

    #Create objects into group_2
    object_2 = Core.make_instance("XObject", "object_2")
    object_2.group = group_2
    session.save(object_2)

    point_2 = Core.make_instance("XPoint", "point_2")
    point_2.group = group_2
    session.save(point_2)

    session.getTransaction.commit
    session.close

    root_id
end

class RESTServer < Sinatra::Base
  def self.init_root(prefix = "/xpca")
    root_id = init_db

    session =  SF.openSession
    session.beginTransaction

    @@root = XGroup.new
    session.load(@@root, root_id.to_java(:long))

    session.getTransaction.commit
    session.close

    @@prefix = prefix
  end

  set :public, File.join(File.dirname(__FILE__), 'public')
  set :views, File.join(File.dirname(__FILE__), 'views')
  init_root

  before do
    @session =  SF.openSession
    @session.beginTransaction
    @root = @@root
    @root.extend(HtmlHelper)
    @root.prefix = @@prefix
  end

  post Regexp.new(@@prefix + "/(.*)") do |path|
    @obj = @@root.getObject(path)
    if @obj
      @obj.extend(JavaHelper)
      @obj.update_fields(params["obj"])
    end
    redirect @@prefix + @obj.fullName
  end

  get Regexp.new(@@prefix + "/(.*)") do |path|
    @obj = @@root.getObject(path)
    @obj.extend(HtmlHelper)
    @obj.prefix = @@prefix
    haml :object_show
  end

  after do
    @session.flush
    @session.getTransaction.commit
    @session.close
  end
end

RESTServer.run!