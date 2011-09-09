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

  it "should update field form Hash" do
    @obj.update_fields(:name => "new_name_object")
    @obj.name.should eql("new_name_object")
  end
end