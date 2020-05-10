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
    //alert("000");
    comment2target(commentId,2,content)
}

/**
 * 展开二级评论事件
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comment = $("#comment-" + id);
    if(!comment.hasClass("in")){    //打开或者折叠二级评论

        var subCommentContainer = $("#comment-"+id);
        //如果二级评论加载过，就不重新请求
        if(subCommentContainer.children().length != 1){
            comment.addClass("in");
            e.classList.add("active");
        }
        //否则请求加载
        else{
            $.getJSON("/comment/"+id,function (data) {
                //循环评论list
                $.each(data.data.reverse(),function (index,comment) {

                    var avatar = $("<img/>",{
                        "class":"media-object img-rounded",
                        "src":comment.user.avatarUrl
                    });
                    var mediaLeft = $("<div/>",{
                        "class":"media-left"
                    }).append(avatar);

                    var nameSpan = $("<span/>",{
                        "text":comment.user.name
                    });
                    var h5 = $("<h5/>",{
                        "class":"media-heading"

                    }).append(nameSpan);

                    var contentDiv = $("<div/>",{
                        "text":comment.content
                    });

                    var dateSpan = $("<span/>",{
                        "class":"pull-right",
                        "text":moment(comment.gmtCreate).format("YYYY-MM-DD")
                    });
                    var menuDiv = $("<div/>",{
                        "class":"menu"
                    }).append(dateSpan);

                    var mediaBody = $("<div/>",{
                        "class":"media-body"
                    });
                    mediaBody.append(h5);
                    mediaBody.append(contentDiv);
                    mediaBody.append(menuDiv);

                    var media = $("<div/>",{
                        "class":"media"
                    });
                    media.append(mediaLeft);
                    media.append(mediaBody);

                    var br = $("<br/>",{
                        "style":"margin-top: 0"
                    });
                    var hr = $("<hr/>",{
                        "style":"margin-top: 0"
                    });
                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12"
                    });
                    commentElement.append(media);
                    commentElement.append(br);
                    commentElement.append(hr);

                    subCommentContainer.prepend(commentElement);

                });
                comment.addClass("in");
                e.classList.add("active");
            });

        }

    }
    else{
        comment.removeClass("in");
        e.classList.remove("active");
    }
    console.log(id);
}