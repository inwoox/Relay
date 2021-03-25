<template>  
  <!-- 아이콘과 인풋을 합쳐서 묶어 업로더 컴포넌트를 만든다. -->
  <div class="fileinput-button">
    <font-awesome-icon :icon="icon"  class="icon" v-if="icon"/> {{ label }}
    <!-- multiple 속성을 통해 네이티브 파일 매니저 창에서 사용자가 여러개의 파일을 선택할 수 있게 한다 -->
    <input :id="id" type="file" name="file" multiple>
  </div>
</template>

<script>
import $ from 'jquery'
import 'jquery-ui/ui/widget'
import 'blueimp-file-upload/js/jquery.fileupload'
import 'blueimp-file-upload/js/jquery.iframe-transport'

export default {
  name: 'Uploader',
  props: ['id', 'url', 'icon', 'label'],
  // 카드 url의 변경을 감지하면, 각각의 행위에 따른 이벤트를 잡아내고, 그에 맞는 uploading 등의 이벤트를 발생시킨다.
  watch: {
    url () {
      if (!this.url) {
        return
      }

      $('#' + this.id).fileupload({
        url: this.url,
        dataType: 'json',

        // 각각의 메서드들은 이벤트 리스너다. / 파일이 업로드 큐에 추가되거나, 실패하거나, 업로드가 완료되거나, 진행 중일 때 호출된다.
        // 즉 파일의 업로드에 관련된 여러 이벤트가 발생할 때, 이 리스너 중에 하나가 이벤트를 받아서, uploading 등의 이벤트를 다시 발생시키고,
        // 그 이벤트는 이 업로더 컴포넌트를 사용하는 상위 컴포넌트에서 다시 받게 된다.
        add: (e, data) => {
          this.$emit('uploading', data.files[0])
          data.submit()
        },
        fail: (e, data) => {
          this.$emit('failed', data._response.jqXHR.responseJSON.message)
        },
        done: (e, data) => {
          this.$emit('uploaded', data.result)
        },
        progress: (e, data) => {
          let progress = parseInt(data.loaded / data.total * 100, 10)
          this.$emit('progress', progress)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.icon {
  margin-right: .5rem;
}
</style>
