<template> 
<div>
  <family-form></family-form>
  </div>
</template>

<script>
  import FamilyForm from '../../components/form/FamilyForm'

  export default {
    layout : "default",
    middleware: ['authenticated'],
    components: {
      FamilyForm
   }
  }
</script>
