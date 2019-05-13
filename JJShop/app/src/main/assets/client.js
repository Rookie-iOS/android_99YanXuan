function showImagePreview(url) {
	var idx = 0;
	console.log(allImgUrls);
	for(var i=0;i<allImgUrls.length;i++){
		if(allImgUrls[i]==url){
			idx= i;
			break;
		}
	}
	var jsonData = "{\"url\":\""+ url +"\",\"index\":"+idx+",\"urls\":\""+allImgUrls+"\"}";
	window.location = "ima-api:action=showImage&data="+jsonData;
}
function getAllImgSrc(htmlstr) {
    var reg=/<img.+?src=('|")?([^'"]+)('|")?(?:\s+|>)/gim;
    var arr = [];
    while(tem=reg.exec(htmlstr)){
        arr.push(tem[2]);
    }
    return arr;
}
var c1=0;
function btnprase(){
if(c1==0){
c1+=1;
var x=document.getElementById("num");
 // alert(x.innerHTML);
 // x.innerHTML+=1;
  var f=x.innerHTML;
  var j = parseInt(f);
  j+=1;
  x.innerHTML=j;
  var praise=document.getElementById("praise");
  Android.shonumber(j,c1);

  }
  function btnunprase(){

  }

}
