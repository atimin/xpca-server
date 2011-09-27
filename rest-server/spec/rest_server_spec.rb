require File.join(File.dirname(__FILE__), 'spec_helper.rb')

describe RESTServer do
  include Rack::Test::Methods

  def app
    @app ||= RESTServer
  end

  before do 
    @path = '/xpca/group_1'
    @grp = ENGINE.getObject(@path)
    @grp.extend(JavaHelper)
  end

  describe "show" do
    before do 
      get @path
    end

    it 'should response ok' do
      last_response.should  be_ok
    end

    it 'should show data' do
      last_response.body.should =~ /#{@grp.id}/
      last_response.body.should =~ /#{@grp.name}/
      last_response.body.should =~ /#{@grp.group.name}/
    end
  end
  
  describe "edit" do
    before do 
      post @path, :obj => { :name => "edited_group" }
    end    

    it 'should edit object' do
      grp = ENGINE.getObject(@grp.group.full_name + "/edited_group")

      grp.id.should eql(@grp.id)
      grp.name.should eql('edited_group')
      grp.group.should eql(@grp.group)
    end

    it 'should redirect to it' do
      last_response.body.should =~ /#{@grp.id}/
      last_response.body.should =~ /edited_group/
    end
  end
end
