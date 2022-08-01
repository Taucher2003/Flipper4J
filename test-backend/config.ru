require "flipper"
require "flipper-api"

Flipper.configure do |config|
  config.adapter do
    Flipper::Adapters::Memory.new
  end
end

run Flipper::Api.app(Flipper)
