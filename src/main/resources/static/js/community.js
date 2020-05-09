/**
 * 提交问题评论
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId,1,content);
}

/**
 * 评论方法
 * @param targetId
 * @param type
 * @param content
 */
function comment2target(targetId,type,content) {
    if(!content){
        alert("评论不能为空");
        return;
    }
    var data = {
        "parentId":targetId,
        "content":content,
        "type":type
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

/**
 * 提交二级评论
 * @param e
 */
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    alert("000");
    comment2target(commentId,2,content)
}

/**
 * 展开二级评论事件(未完成)
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comment = $("#comment-" + id);
    if(!comment.hasClass("in")){    //打开或者折叠二级评论

        $.getJSON("/comment/"+id,function (data) {

            var commentBody = $("#comment-body-"+id);
            var items = [];

            $.each(data.data,function (comment) {
                var c = $("<div/>",{
                    "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12",
                    html:comment.content
                });
                items.push(c);

            });

            $("<div/>",{
                "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse sub-comments",
                "id":"comment-"+id,
                html:items.join("")
            }).appendTo(commentBody);


            comment.addClass("in");
            e.classList.add("active");
            console.log(data);
        });

    }
    else{
        comment.removeClass("in");
        e.classList.remove("active");
    }
    console.log(id);
}