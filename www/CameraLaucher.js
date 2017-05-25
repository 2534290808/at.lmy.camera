var exec = require('cordova/exec');
var CameraLaucher = {
	openCamera : function(onSuccess, onError, options) {
		var text = 'none';// 文字默认为none
		var size = 70;
		var position = 'CENTER';// 位置默认为中间
		var color = -1;// 字体颜色默认为白色
		var saveToPictureAlbum = false;// 是否保存到相册，默认为FALSE
		if (typeof (options) != 'undefined') {
			if (typeof (options['text']) != 'undefined') {
				text = options.text;
			}
			if (typeof (options['size']) != 'undefined') {
				size = options.size;
			}
			if (typeof (options['position']) != 'undefined') {
				position = options.position;
			}
			if (typeof (options['color']) != 'undefined') {
				color = options.color;
			}
			if (typeof (options['saveToPictureAlbum']) != 'undefined') {
				saveToPictureAlbum = options.saveToPictureAlbum;
			}
		}
		exec(onSuccess, onError, "CameraActivity", "openCamera",[text,size,position,color,saveToPictureAlbum]);
	},
	test:function(onSuccess, onError){
		exec(onSuccess,onError,"CameraActivity","test",[]);
	}
}
module.exports = CameraLaucher;