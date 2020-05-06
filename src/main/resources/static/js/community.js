function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
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
                $("#comment_section").hide();
            }
            else{
                alert(result.message);
            }
            console.log(result);
        },
        dataType: "json"
    });
}