<template>
  <div class="px-4 flex flex-col items-center">
    <div
      class="w-80 h-48 rounded-lg"
      v-bind:style="{
        backgroundImage: 'url(' + getThumbnail(info.ytlink) + ')',
        backgroundPosition: 'center',
        backgroundSize: 'cover',
      }"
    ></div>

    <div class="cursor-pointer font-medium text-lg self-start pl-4 pt-2">
      {{ info.name }}
    </div>

    <div class="flex flex-row w-full px-6 pt-2 justify-between">
      <div>{{ info.views }} <font-awesome-icon icon="eye" /></div>
      <div class="flex">
        <div class="pr-4">
          <font-awesome-icon icon="thumbs-up" class="icon alt" />
          {{ info.likes }}
        </div>
        <div>
          <font-awesome-icon icon="thumbs-down" class="icon alt" />
          {{ info.dislikes }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  info: {
    type: Object,
  },
});

function getThumbnail(ytlink) {
  const regexLink =
    /http(?:s?):\/\/(?:www\.)?youtu(?:be\.com\/watch\?v=|\.be\/)([\w\-\_]*)(&(amp;)?‌​[\w\?‌​=]*)?/;
  const match = ytlink.match(regexLink);
  console.log(match[1]);
  return "http://i3.ytimg.com/vi/" + match[1] + "/hqdefault.jpg";
}
</script>
