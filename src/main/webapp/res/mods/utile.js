/**
 * Created by Administrator on 2018/12/7.
 */

var result=0;
$(function () {
    var firstNum=Math.floor(Math.random()*20)+1;
    var SecondNum=Math.floor(Math.random()*20)+1;
    result=firstNum+SecondNum;
    $("#spanRenlei").text(firstNum+"+"+SecondNum+"=?");
});

$(function () {
    $("#L_vercode").blur(function () {
        if($("#L_vercode").val() != result){
            $("#checkRenlei").text("答案错误");
            $("#L_vercode").val("")
        }else {
            $("#checkRenlei").text("");
        }
    })
});

$(function () {
    $("button[class=layui-btn]").click(function () {
        if($("#checkRenlei").text() == "答案错误"){
            alert("答案错误,重新回答");
            $("#checkRenlei").text("");
            return false;
        }
    })
})
