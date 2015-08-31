# clj-aws

A small Clojure App that lists the ASGs and their vpcs in a much simpler format than Amazon's
AWS web interface.

## Configuration
Create a `profiles.clj` file in the same format as `profiles.sample.clj` and add
your aws key and secret. __DO NOT COMMIT__ this file to the git repo.

## Running
Start Cider in emacs then switch to the `repl` namespace and run `(reset)`.

Start figwheel in the commandline: `lein figwheel`.

Open the app at `http://localhost:9009`



## License
Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
