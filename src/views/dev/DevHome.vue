<script lang="ts" setup>

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faGlobe, faScroll } from '@fortawesome/free-solid-svg-icons';
</script>

<template>
  <div class="flex flex-col gap-3">
    <router-link to="/dev/globals"><FontAwesomeIcon :icon="faGlobe" /> Globals</router-link>
    <router-link to="/dev/scroller"><FontAwesomeIcon :icon="faScroll" /> Scroller</router-link>
  </div>
</template>