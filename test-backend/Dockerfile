FROM ruby:2.7.5-buster
COPY config.ru Dockerfile Gemfile Gemfile.lock ./
RUN gem install bundler
RUN bundle install
CMD ["bundle", "exec", "puma", "-t", "1:1"]
