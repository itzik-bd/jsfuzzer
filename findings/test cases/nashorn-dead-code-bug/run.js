var x = "string";
print(x);

(function () {
	(function () {
		// print is undefined!
		print(x);
	})();

	if (false) {
		(function () {
			var x;
		})();
	}
})();