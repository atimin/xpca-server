require 'java'

$: << File.join(File.dirname(__FILE__),'..')
$: << File.join(File.dirname(__FILE__),'lib')
require 'helpers/java_helper'

module PackageHelper
  include_package "net.flipback.xpca.core"
  
  def get_subclasses(obj)
    
  end
  
  def get_class(class_name)
    eval class_name
  end
  
  def get_instance(class_name, *args)
    o = get_class(class_name).send "new", *args
    o.extend(JavaHelper) 
    o
  end
end