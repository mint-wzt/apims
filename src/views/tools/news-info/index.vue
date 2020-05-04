<template>
  <elFrame src="http://nc.mofcom.gov.cn/channel/information/zxIndex.shtml" />
</template>
<script>
import { mapGetters } from 'vuex'
import elFrame from '@/components/Iframe/index'
export default {
  name: 'NewsInfo',
  components: { elFrame },
  computed: {
    ...mapGetters([
      'swaggerApi'
    ])
  }
}
</script>
