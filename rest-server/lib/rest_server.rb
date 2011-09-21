require 'sinatra'
require 'haml'
require 'java'

require 'helpers/html_helper'
require 'helpers/java_helper'
require 'helpers/package_helper'

java_import "net.flipback.xpca.core.Engine"
java_import "org.hibernate.cfg.Configuration"

ENGINE = Engine.new(Configuration.new.configure().buildSessionFactory(), "/xpca")

CORE = PackageHelper.new("net.flipback.xpca.core")

#init ENGINE (temp resolve)
ENGINE.addObject "/xpca",    CORE.make_instance("XGroup", "group_1")

ENGINE.addObject "/xpca/group_1", CORE.make_instance("XObject", "xobj_1")
ENGINE.addObject "/xpca/group_1", CORE.make_instance("XObject", "xobj_2")
ENGINE.addObject "/xpca/group_1", CORE.make_instance("XPoint", "xpoint_1")
ENGINE.addObject "/xpca",         CORE.make_instance("XGroup", "group_2")
ENGINE.addObject "/xpca/group_2", CORE.make_instance("XObject", "xobj_1")
ENGINE.addObject "/xpca/group_2", CORE.make_instance("XObject", "xobj_2")
ENGINE.addObject "/xpca/group_2", CORE.make_instance("XPoint", "xpoint_1")
ENGINE.commit

class RESTServer < Sinatra::Base
  set :public, File.join(File.dirname(__FILE__), 'public')
  set :views, File.join(File.dirname(__FILE__), 'views')

  before do
    @root = ENGINE.getRoot
    @root.extend(HtmlHelper)
  end

  post /(\/.*)/ do |path|
    @obj = ENGINE.getObject(path)
    if @obj
      @obj.extend(JavaHelper)
      @obj.from_hash(params["obj"])
      ENGINE.updateObject(path, @obj)
    end
    redirect @obj.fullName
  end

  get /(\/.*)/ do |path|
    puts path
    @obj = ENGINE.getObject(path)
    puts @obj
    @obj.extend(HtmlHelper)
    haml :object_show
  end
end

RESTServer.run!