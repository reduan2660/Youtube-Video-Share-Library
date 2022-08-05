<template>
  <div class="px-4 py-4 flex justify-between">
    <div class="font-bold">Video Library</div>

    <div class="flex items-center justify-center">
      <Btn @click="upload">
        <font-awesome-icon icon="circle-plus" /> Upload Video</Btn
      >
      <div class="ml-10 mr-4">
        <font-awesome-icon icon="user" />
      </div>
    </div>
  </div>
</template>

<script setup>
import Btn from "./Btn.vue";

function upload() {}
</script>
