require 'java'
require File.join(File.dirname(__FILE__), "package_helper")

java_import  'net.flipback.xpca.annotations.AnnotaionAccessor'
java_import  'net.flipback.xpca.annotations.Field'

module HtmlHelper
  def html_tree
    html = "<ul id='tree_objects'>"
    html << html_link

    self.getChildren.each  do |o|
      o.extend(HtmlHelper)
      html << "<li>"
      if(o.instance_of? XGroup)
        html << o.html_tree
      else
        html << o.html_link
      end
      html << "</li>"
    end

    html << "</ul>"
  end

  def html_root_tree
    root = self.getRoot
    root.extend(HtmlHelper)
    root.html_tree
  end

  def html_show
    html = ""
    field_names = AnnotaionAccessor.getFieldNames(self)
    AnnotaionAccessor.getFieldValues(self).each_pair do |f,o|
      html << f.title + ":\t"
      case f.inputType
      when Field::InputType::TEXT then
        html << "<input type='text' name='obj[#{field_names[f]}]' value='#{o.to_s}'/>"
      else
        if o.class <= XObject
          o.extend(HtmlHelper)
          html << o.html_link
        else
          html << o.to_s
        end
      end
      html << "<br/>"
    end

    pkg = PackageHelper.new("net.flipback.xpca.core")
    html << "<select name[class]>"
    pkg.classes.each do |kl|
      html << "<option value='#{kl.name.split("::")[-1]}'>#{kl.name.split("::")[-1]}</option>" if kl <= pkg.get_class("XObject")
    end
    html << "</select>"
    html
  end

  def html_link
    "<a href='#{self.getFullName}'>#{self.getName}</a>"
  end
end
