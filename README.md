# mcljs

A very simple project skeleton using Reagent, Austin, Selmar and a few
other libs to create a comfortable environment for working with clojurescirpt

## Installation

Just clone the repo and go for it!

## Usage

To use, clone the repo and then from the root directory run

    `$ lein repl``

then from the repl, run

    `(start-server)`

to start a basic jetty server on port 3000. You can call with an argument
specifying the port if desired.

Once the clojure repl is running and you have started the server, you can then start
a clojurescript browser repl by running

    `(cljs!)`

## Background

I wanted to use Austin as my brepl, but I didn't want to include the enlive library
as I prefer to use Selmar and templates. Nearly all the examples I could find which
used Austin use enlive to modify pages as they are served to include the necessary
javascript required to tell the browser where to connect to the repl.

I also wanted to have fairly clear separation between production and development
environments. In particular, I didn't want any of the development stuff embedded in
my production system.

This is as much a learning experinece for me as anything else. I've played with a
number of clojure web frameworks and various lein templates. All were pretty good and
a great way of learning. However, my view is that the real power of clojure and
clojurescript is in the ability to roll your own environment which suits your
preferred workflows rather than spending time and effort learning someone elses ideal
framework and adjusting your workflow to suit their views and approach.

I'm new to web programming and javascript. To help guide my learning experience, I've
been using the excellent *Modern Clojurescript* tutorial. However, my approach has
been a little different. Rather than just following along with the examples, I've
decided to follow along witht he problems/exercises, but solve them using the
libraries and approaches which I work out independnetly. In some cases, my decisions
are guided by google nd various blogs. At the end, I hope to hae both sufficient
understanding to use clojure and clojurescript effectively and be able to define my
own lein template for new projects which include all the libraries I prefer.

## Thanks

None of this would have been possible without the excellent contributions from many
in the Clojure and ClojureScript community. My contributions are
insignificant. However, often, even the most insignificant contribution may contain
that one idea or provide just that little bit of crucial but missing information
which pulls it all together. I hope that at least one person finds something of use
here and if not, few feel it has wasted their time!

Far too many people have helped me on my path to clojure and ClojureScript. However,
a few projectws have been increadilbly useful.

    * Luminos Web Framework http://www.luminusweb.net/
    * Modern Clojurescript https://github.com/magomimmo/modern-cljs
    * Reagent Seed Template https://github.com/gadfly361/reagent-seed
    * Reagent Template https://github.com/reagent-project/reagent-template
    * Austin https://github.com/cemerick/austin

## Feedback

Feedback is always welcome! This is just an effort from someone trying to get to
grips with Clojure and ClojureScript. I *know* there are errors, bad style and
incorrect idioms here - let me know so that I can fix them!

## License

Copyright © 2014 Tim Cross

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
