var x;

(function ()
{
	(function ()
	{
		// print is undefined!
		print("hello");
		return x;
	})();

	if (false)
	{
		(function ()
		{
			var x;
		})();
	}
})();