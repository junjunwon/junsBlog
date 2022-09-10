package com.junsblog.repository;

import com.junsblog.domain.Post;
import com.junsblog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getPageList(PostSearch postSearch);
}
