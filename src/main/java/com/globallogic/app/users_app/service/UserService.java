package com.globallogic.app.users_app.service;

import com.globallogic.app.users_app.model.dto.request.SignUpRequest;
import com.globallogic.app.users_app.model.dto.response.UserResponse;

public interface UserService {
  public UserResponse signUp(SignUpRequest request);
  public UserResponse login(String requestToken);

}
