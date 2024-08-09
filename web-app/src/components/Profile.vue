<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-sheet
          rounded="lg">
          <v-container class="d-flex justify-center pt-12 pb-7">
            <v-hover v-slot="{ isHovering, props }">
              <v-avatar
                variant="elevated"
                v-bind="props"
                color="grey"
                size="175">
                <v-img cover :src="user!.picture"/>
                <v-overlay
                  :model-value="isHovering"
                  contained
                  class="align-center justify-center">

                  <input
                    @change="handleFileUpload"
                    type="file"
                    accept="image/png, image/jpeg, image/bmp"
                    ref="profile_pic"
                    style="display: none"/>

                  <v-btn
                    icon="mdi-camera-outline"
                    color="primary"
                    flat
                    @click="($refs.profile_pic as any).click()"/>

                </v-overlay>
              </v-avatar>
            </v-hover>
          </v-container>

          <v-form @submit.prevent>
            <v-container>
              <v-row class="ga-2 justify-center" no-gutters>
                <v-col cols="8">
                  <v-text-field
                    v-model="user!.nickname"
                    color="primary" label="Nickname" variant="outlined"/>
                </v-col>

                <v-col cols="8">
                  <v-text-field
                    v-model="user!.name"
                    color="primary" label="Name" variant="outlined"/>
                </v-col>

                <v-col cols="8">
                  <v-text-field
                    v-model="user!.email"
                    color="primary" label="Email" variant="outlined"/>
                </v-col>

                <v-col cols="8" class="d-flex justify-center ga-3 mt-3 mb-7">
                  <v-btn height="50px" width="175px" @click="resetPassword" type="submit" variant="outlined"
                         color="primary">
                    Password reset
                  </v-btn>
                  <v-btn height="50px" width="175px" @click="updateUser" type="submit" color="primary">
                    Update profile
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-form>

        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import {useAuth0} from '@auth0/auth0-vue';
import {UserData} from "../../models/UserData";
import {UsersClient} from "../../api/clients/UsersClient";
import {GlobalSnackbar} from "@/store/Snackbar";

const snackbar = GlobalSnackbar();
const {logout, user, getAccessTokenSilently} = useAuth0();

function handleFileUpload(event: any) {
  const file = event.target.files[0]; // Get the uploaded file
  console.log(file)
}

async function updateUser() {
  const data = new UserData(
    user.value?.sub,
    user.value?.name,
    user.value?.email,
    user.value?.nickname);

  const client = new UsersClient(getAccessTokenSilently);

  if (await client.updateUser(data)) {
    snackbar.show("mdi-hand-okay", "success", "Profile updated correctly");
  } else {
    snackbar.show("mdi-close-box-outline", "error", "An error occurred.");
  }
}

async function resetPassword() {
  const client = new UsersClient(getAccessTokenSilently);

  if (await client.resetPassword(user.value?.email)) {
    snackbar.show("mdi-hand-okay", "success", "Password correctly reset");
    await logout({logoutParams: {returnTo: window.location.origin}})
  } else {
    snackbar.show("mdi-close-box-outline", "error", "An error occurred.");
  }
}
</script>
