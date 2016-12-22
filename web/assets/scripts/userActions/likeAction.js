
$( ".post-image-container" ).on('doubleTap', function() {
	$(this).parent('div').siblings('.post-footer').children('div').children('img').attr('src', 'assets/images/heart_fill.png');

});

$( ".post-image-container" ).dblclick(function() {
	$(this).parent('div').siblings('.post-footer').children('div').children('img').attr('src', 'assets/images/heart_fill.png');
});

