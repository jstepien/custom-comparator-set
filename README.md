# Custom Comparator Sets in Clojure

**Alpha** A custom Clojure/Script type for custom comparator sets.

### Rationale

When dealing with domain data in maps this data often has unique
identifiers. To make merging easy and guarantee that there are no two
items with the same identifier people often store those models in maps
of the following format:

```clojure
{(:id model) model
 ,,,}
```

While this certainly is a solution it complicates the handling of the
data in every place imaginable. Often you just want `(vals my-models)`
instead of the actual map. Adding new items requires code that is
tightly coupled to the underlying map that you only chose to guarantee
uniqueness of a certain property (`(assoc my-models (:id model)
model)`).

### Custom Sets

This library provides a custom type `CustomComparatorSet` which allows
users to manage data in a set-like datastructure while using a custom
function as uniqueness comparator.

```clojure
;; (import [org.martinklepsch.cc_set.impl CustomComparatorSet])            ; Clojure
;; (require '[org.martinklepsch.cc-set :refer [custom-comparator-set]]) ; ClojureScript
(def x (custom-comparator-set :id))
(conj x {:id 1} {:id 2})
;; => #CustomComparatorSet{{:id 1} {:id 2}}
```

> While a keyword `:id` is used as comparator here you can also use
> more complex functions.

A constructor `set-by` is provided which additionally wraps the passed
comparator in a nil check, throwing if `nil` is returned.

```clojure
(require '[org.martinklepsch.cc-set :refer [set-by]])
(set-by :id {:id "a"} {:id "x"} {:id "b"})
```

If you don't want the `nil` guard behavior you can implement your own constructor like this:

```clojure
(defn my-set-by [comparator & keys]
  (reduce conj (CustomComparatorSet. {} comparator) keys)))
```

Simple!

### Low level constructors

Clojure

```
(import [org.martinklepsch.cc_set.impl CustomComparatorSet])
(CustomComparatorSet. {} :id)
```

ClojureScript

```
(require '[org.martinklepsch.cc-set.impl :refer [CustomComparatorSet]])
(CustomComparatorSet. nil {} :id)
```
**Notice the different arity, resulting of the differences in how
  Clojure and ClojureScript handle metadata.**

#### Differences to regular sets

Lookups via `(get ccset k)` look for entries where the comparator
returned `k`.  This way you can still effectively look up entries by
their unique property without traversing the full set.

### Building and Testing

```sh
boot build-jar           # install to ~/.m2
boot test-cljc           # run tests once
boot watch test-cljc     # run tests after changes
```

### Contributing

Feedback and PRs welcome, especially generative testing seems like a good fit.

--

### License

MPLv2, see `LICENSE`.
