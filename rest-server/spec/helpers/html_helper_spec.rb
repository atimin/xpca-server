require "java"
require "helpers/html_helper"

java_import "net.flipback.xpca.core.XObject"
java_import "net.flipback.xpca.core.XGroup"

describe HtmlHelper do
  before(:each) do
    @root = XGroup.new("/xpca")
    @obj = XObject.new("test_obj")
    @obj.setGroup(@root)
    @obj.setId(1)
    @root.getChildren << @obj

    @obj.extend(HtmlHelper)
  end

  it "should make html link" do
    @obj.html_link.should eql("<a href='/xpca/test_obj'>test_obj</a>")
  end

  it "should make html for root" do
    @obj.html_root_tree.should eql("<ul id='tree_objects'><a href='/xpca'>/xpca</a>" +
      "<li><a href='/xpca/test_obj'>test_obj</a></li>" +
      "</ul>"
    )
  end

  it "should make html form" do
    @obj.html_show.should eql("ID:\t1<br/>" +
      "Name:\t<input type='text' name='obj[name]' value='test_obj'/><br/>" +
      "Group:\t<a href='/xpca'>/xpca</a><br/>" +
      "<selected name[class]></selected>"
    )
  end
end