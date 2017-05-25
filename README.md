此插件是一个拍照插件，其最大的特点就是可以在照相的同时给图片上添加水印,此插件只适用于Android平台

提供的API:
函数：openCamera(onSuccess,onError,options);

function onSuccess(imageUrl){//返回图片的存储路径

}
function onError(){

}
options为json格式的数据，可设置有：

text：//相片水印的文本

size：//文字的尺寸默认为70

position：//文字所处图片位置可选有：
Camera.watermark.Postion.CENTER(默认为CENTER)
Camera.watermark.Postion.LEFT_TOP
Camera.watermark.Postion.LEFT_BOTTOM
Camera.watermark.Postion.RIGHT_TOP
Camera.watermark.Postion.RIGHT_BOTTOM


color://文字的颜色可选有
Camera.watermark.Color.WHITE/默认为WHITE
Camera.watermark.Color.BLACK
Camera.watermark.Color.BLUE
Camera.watermark.Color.YELLOW
Camera.watermark.Color.CYAN
Camera.watermark.Color.DKGRAY
Camera.watermark.Color.GRAY
Camera.watermark.Color.GREEN
Camera.watermark.Color.LTGRAY
Camera.watermark.Color.MAGENTA
Camera.watermark.Color.RED
Camera.watermark.Color.TRANSPARENT

saveToPictureAlbum:是否保存到相册 true/false,默认为false;