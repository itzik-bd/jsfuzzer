var myPrint = function(str)
{
	typeof console !== 'undefined' ? console.log(str) : print(str);
};

var v1;

function f1()
{
	v1 = 1;
	return true;
	(function () { var v1; })();
}

myPrint(typeof(f1));
myPrint(f1);
f1();