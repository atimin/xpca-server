require 'java'
require "helpers/java_helper"

java_import "net.flipback.xpca.core.XObject"

describe JavaHelper do
  before(:each) do
    @obj = XObject.new("name_objects")
    @obj.extend(JavaHelper)
  end

  it "should get field in ruby style" do
    @obj.name.should eql("name_objects")
  end

  it "should set field in ruby style"  do
    @obj.name = "new_name_object"
    @obj.name.should eql("new_name_object")
  end

  it "should set fields from Hash" do
    @obj.from_hash(:name => "new_name_object")
    @obj.name.should eql("new_name_object")
  end

  it "should get fields to Hash" do
    @obj.to_hash.should eql(:name => "name_objects", :full_name => "name_objects", :class => @obj.getClass)
  end
end