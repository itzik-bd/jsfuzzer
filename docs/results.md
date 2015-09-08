# Bugs that JsFuzzer found

## DynJs - [github](https://github.com/dynjs/dynjs)
* [Logical not of number 2^n (where n=32,...) yields true instead of false](https://github.com/dynjs/dynjs/issues/156)
* [can't push undefined value to array](https://github.com/dynjs/dynjs/issues/157) 
* [JSON.stringify of a NaN value](https://github.com/dynjs/dynjs/issues/158)

## Nashorn
* dead code elimination corrupts global scope (JI-9023716)
* dead code elimination in js file causes an exception during runtime (JI-9023808)


## Rhino
 * [Wrong result of minus-minus expression](https://github.com/mozilla/rhino/issues/237)
