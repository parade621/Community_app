package com.parade621.community_app.board

/**
 * 댓글의 데이터를 받아오기 위한 데이터 클래스이다.
 */

data class CommentModel (
    val comment:String ="",
    val uid:String = "",
    val time:String =""
        )