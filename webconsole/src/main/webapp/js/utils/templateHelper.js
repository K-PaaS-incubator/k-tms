define(function(require) {
	
	var $ = require('jquery');
	
	return {
		
		isHighLight: function(type) {
			
			return type == this.highLightEl ? 'colgroup-highlight' : '';

		}
		
	};
	
});