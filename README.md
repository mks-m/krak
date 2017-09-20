# krak

[Kraken API](https://www.kraken.com/help/api) in Clojure.

## Installation

Add the following to your project.clj's dependencies:

[![Clojars krak](https://img.shields.io/clojars/v/krak.svg)](https://clojars.org/krak)

## Dependencies

- org.clojure/clojure
- org.clojure/data.codec
- org.clojure/data.json
- http-kit
- pandect

## Examples

```clojure
(require '[krak.client :as k])

(get-in (k/time) [:data "result" "unixtime"])
;; => 1505839538

(get-in (k/asset-pairs :pair ["BCHEUR"]) [:data "result" "BCHEUR" "fees_maker" 0])
;; => [0 0.16]

;; setup client context with API credentials and optional
;; nonce offset (nonce-offset named argument)
(def kctx (k/new-context api-key secret-key))

(get-in (k/balance kctx) [:data "result"])
;; => {"XXBT" "millions", "XETH" "billions"}
```

## License

Copyright 2017 Maksym Melnychok

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
