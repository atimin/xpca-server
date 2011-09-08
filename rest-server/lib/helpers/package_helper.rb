require 'java'

module J
  java_import "java.lang.ClassLoader"
end

require 'helpers/java_helper'

class PackageHelper
  attr_reader :package

  def initialize(package)
    @package = package

    rel_path = File.join(@package.split("."))
    resource = J::ClassLoader.getSystemClassLoader().getResource(rel_path)

    unless resource
      raise RuntimeError.new "No resource for #{rel_path}"
    end

    directory = Dir.new(resource.getPath())
    classes_list = directory.entries.select {|f| f =~ /[a-zA-Z]\.class/ }

    @classes = {}
    classes_list.each do |c|
      c[".class"] = ""
      begin
        java_import [@package, c].join(".")
        @classes[c.to_sym] = eval(c)
      rescue NameError => e
        puts "WARNING: #{e}"
      end
    end
  end

  def classes
    @classes.values
  end

  def get_class(class_name)
    @classes[class_name.to_sym]
  end

  def make_instance(class_name, *args)
    o = get_class(class_name).send "new", *args
    o.extend(JavaHelper)
    o
  end
end