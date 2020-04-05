<template>
  <div>
    <svg-icon class-name="size-icon" icon-class="address" />{{ user.region }}
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Address',
  computed: {
    ...mapGetters([
      'user'
    ])
  }
}
</script>

<style scoped>

</style>
