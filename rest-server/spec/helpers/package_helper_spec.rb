require "helpers/package_helper"

describe PackageHelper do
  before(:all) do
    @pack = PackageHelper.new("net.flipback.xpca.core")
  end

  it "should get class by name" do
    @pack.get_class("XObject").should eq(Java::NetFlipbackXpcaCore::XObject)
  end

  it "should get instance by name class" do
    xobj = @pack.make_instance("XObject", "object_name")
    xobj.class.should eql(Java::NetFlipbackXpcaCore::XObject)
    xobj.name.should eql("object_name")
  end

  it "should get classes from package" do
    @pack.classes.include?(Java::NetFlipbackXpcaCore::XObject).should be_true
    @pack.classes.include?(Java::NetFlipbackXpcaCore::XGroup).should be_true
  end
end