require 'sinatra'
require 'haml'
require 'java'

$: << File.join(File.dirname(__FILE__),'lib')
require 'helpers/html_helper'

$: << File.join(File.dirname(__FILE__),'..')

java_import 'org.hibernate.cfg.Configuration'
java_import 'net.flipback.xpca.core.XGroup'
java_import 'net.flipback.xpca.core.XObject'
java_import 'net.flipback.xpca.core.XPoint'



SF = Configuration.new.configure.buildSessionFactory

def init_db
    session =  SF.openSession  
    session.beginTransaction
    # Create root group
    root = XGroup.new("/")
    root_id = session.save(root)
    # Create groups into root
    group_1 = XGroup.new("group_1")
    group_1.setGroup(root)
    session.save(group_1)
    
    group_2 = XGroup.new("group_2")
    group_2.setGroup(root)
    session.save(group_2)
    
    #Create objects into group_1
    object_1 = XObject.new("object_1")
    object_1.setGroup(group_1)
    session.save(object_1)
    
    point_1 = XPoint.new("point_1")
    point_1.setGroup(group_1)
    session.save(point_1)
    
    #Create objects into group_2
    object_2 = XObject.new("object_2")
    object_2.setGroup(group_2)
    session.save(object_2)
    
    point_2 = XPoint.new("point_2")
    point_2.setGroup(group_2)
    session.save(point_2)
    
    session.getTransaction.commit
    session.close
    
    #
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

  get %r{/xpca/(.*)} do |path|
    @obj = @@root.getObject(path)
    @obj.extend(HtmlHelper)
    @obj.prefix = @@prefix
    haml :object_state
  end
  
  after do
    @session.getTransaction.commit
    @session.close
  end
end

RESTServer.run!