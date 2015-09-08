# JSFuzzer Code Style Conclusions

"this" keyword
---
"this" keyword refers to different objects in different engines in global program scope. Therefore don't use "this" keyword in other-most program scope.

Functions Definitions
---
***Do not*** define function in control-flow instructions (if-else, while, for etc.)
```
if (false) {
	function f() {
		return 5;
	}
}
f();
```
Some engines recognize ```f``` as a function while others do not.

---

***Do:***

* Define functions in global scope
* Define nested functions in outer-most scope of containing function
	```
	function f() {
		function g() {
			return 1;
		}
		g();
	}
	```
  
* Use anonymous function in control-flow instructions
	```
	var f;
	if (false) {
		f = function () {
			return 5;
		}
	}
	f();
	```
	This code will never recognize ```f``` as function.
