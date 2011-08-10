var wig = {
	show: function(path, version) {
		document.location.href = 
			(path.charAt(0) == '/' ? path : '/' + path)
			+ (version ? '.v' + version : '');
		return false;
	},

	preview: function() {
		alert('not implemented yet!');
		return false;
	}
}
window.wig = wig;