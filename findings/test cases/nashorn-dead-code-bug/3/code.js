var myPrint = function(str)
{
	typeof console !== 'undefined' ? console.log(str) : print(str);
};

var u;

var func = function()
{
	var x;
	
	(function ()
	{
		(function ()
		{
			u = undefined;
		})(x);
	})();	
		
	return;
	
	(function (){ var u; })();
};

func();

var str = "Nashorn!";

myPrint("Who ARE We? " + str);

func();
	
myPrint("Who ARE We? " + str);