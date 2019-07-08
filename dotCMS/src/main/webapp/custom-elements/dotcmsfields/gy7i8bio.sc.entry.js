const t=window.dotcmsFields.h;import{a as e}from"./chunk-1d89c98b.js";import{a as s,b as i,c as a,d as l,e as h,f as r,g as n,h as o,i as u,j as d}from"./chunk-098a701f.js";class c{constructor(){this.disabled=!1,this.name="",this.label="",this.hint="",this.options="",this.required=!1,this.requiredMessage="This field is required",this.value="",this._dotTouched=!1,this._dotPristine=!0}componentWillLoad(){this.validateProps(),this.emitInitialValue(),this.status=s(this.isValid()),this.emitStatusChange()}optionsWatch(){const t=i(this,"options");this._options=a(t)}hostData(){return{class:l(this.status,this.isValid(),this.required)}}reset(){this.value="",this.status=s(this.isValid()),this.emitInitialValue(),this.emitStatusChange()}render(){return t(e,null,t("dot-label",{label:this.label,required:this.required,name:this.name},t("select",{"aria-describedby":h(this.hint),class:r(this.status.dotValid),id:n(this.name),disabled:this.shouldBeDisabled(),onChange:t=>this.setValue(t)},this._options.map(e=>t("option",{selected:this.value===e.value||null,value:e.value},e.label)))),o(this.hint),u(!this.isValid(),this.requiredMessage))}validateProps(){this.optionsWatch()}shouldBeDisabled(){return!!this.disabled||null}setValue(t){this.value=t.target.value,this.status=d(this.status,{dotTouched:!0,dotPristine:!1,dotValid:this.isValid()}),this.emitValueChange(),this.emitStatusChange()}emitInitialValue(){this.value||(this.value=this._options.length?this._options[0].value:"",this.emitValueChange())}emitStatusChange(){this.statusChange.emit({name:this.name,status:this.status})}isValid(){return!this.required||!!this.value}emitValueChange(){this.valueChange.emit({name:this.name,value:this.value})}static get is(){return"dot-select"}static get properties(){return{_options:{state:!0},disabled:{type:Boolean,attr:"disabled",reflectToAttr:!0},el:{elementRef:!0},hint:{type:String,attr:"hint",reflectToAttr:!0},label:{type:String,attr:"label",reflectToAttr:!0},name:{type:String,attr:"name",reflectToAttr:!0},options:{type:String,attr:"options",reflectToAttr:!0,watchCallbacks:["optionsWatch"]},required:{type:Boolean,attr:"required",reflectToAttr:!0},requiredMessage:{type:String,attr:"required-message",reflectToAttr:!0},reset:{method:!0},status:{state:!0},value:{type:String,attr:"value",reflectToAttr:!0,mutable:!0}}}static get events(){return[{name:"valueChange",method:"valueChange",bubbles:!0,cancelable:!0,composed:!0},{name:"statusChange",method:"statusChange",bubbles:!0,cancelable:!0,composed:!0}]}static get style(){return""}}export{c as DotSelect};