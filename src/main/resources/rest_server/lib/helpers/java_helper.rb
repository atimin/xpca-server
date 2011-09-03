module JavaHelper
  def update_fields(attrs)
    attrs.each_pair do |name, value|
      send name + "=", value
    end
  end
  
  def method_missing(meth, *args, &blk)
    f = meth.to_s
    if f[-1] == '='
      f = "set" + f[0..-2].capitalize
      if public_methods.include?(f)
        return send f, *args
      end         
    else
      f = "get" + f[0..-1].capitalize
      if public_methods.include?(f)
        return send f, *args
      end     
    end
    raise NoMethodError.new, "undefined method `#{meth}' for #{self.inspect}:#{self.class}"
  end
end