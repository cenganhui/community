/**
 * 提交评论回复事件
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    if(!content){
        alert("评论不能为空");
        return;
    }

    console.log(questionId);
    console.log(content);
    var data = {
        "parentId":questionId,
        "content":content,
        "type":1
    };
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify(data),
        success: function (result) {
            if(result.code == 200){
                //提交成功则刷新页面
                location.reload();
            }
            else{
                if(result.code == 2003){    //用户未登录
                    //提示是否登录
                    var isAccepted = confirm(result.message);
                    //跳转登录，登录后关闭登录页面
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=81388564db4884cddfe9&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                }
                else{
                    alert(result.message);
                }
            }
        },
        dataType: "json"
    });
}