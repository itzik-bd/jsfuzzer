myPrint = function(str)
{
	typeof console !== 'undefined' ? console.log(str) : print(str);
};


(function ()
{
	var v;

	myPrint("v   " + v);
	myPrint("v-- " + v--);
})();

myPrint("\n\nExecution is over.\n\n");
