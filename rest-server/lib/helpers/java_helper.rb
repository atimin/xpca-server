module JavaHelper
  def from_hash(attrs)
    attrs.each_pair do |name, value|
      send name.to_s + "=", value
    end
  end

  def to_hash
    hash = {}
    public_methods.each do |meth|
      if meth[0,4] == "get_"
        value = send(meth)
        hash[meth[4..-1].to_sym] = value if value
      end
    end
    hash
  end

  def method_missing(meth, *args, &blk)
    f = meth.to_s
    if f[-1] == '='
      f = "set_" + f
      if public_methods.include?(f)
        return send f, *args
      end
    else
      f = "get_" + f
      if public_methods.include?(f)
        return send f, *args
      end
    end
    raise NoMethodError.new, "undefined method `#{meth}' for #{self.inspect}:#{self.class}"
  end
end
