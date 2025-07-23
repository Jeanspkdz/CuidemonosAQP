package com.jean.cuidemonosaqp.modules.notification.data.remote

import com.jean.cuidemonosaqp.modules.notification.data.dto.UserSafeZoneInvitationResponseDto
import com.jean.cuidemonosaqp.modules.notification.data.dto.UserSafeZoneInvitationUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface UserSafeZoneInvitationApi {
    @GET("/safe-zone-invitation/{id}")
    suspend fun getUserSafeZoneInvitations(@Path("id") userId: String): Response<List<UserSafeZoneInvitationResponseDto>>


    @PUT("/safe-zone-invitation/{id}")
    suspend fun updateUserSafeZoneInvitation(
        @Path("id") id: String,
        @Body invitation: UserSafeZoneInvitationUpdateDto
    ): Response<UserSafeZoneInvitationResponseDto>
}