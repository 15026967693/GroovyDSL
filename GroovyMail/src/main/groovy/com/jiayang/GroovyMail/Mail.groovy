package com.jiayang.GroovyMail

class Mail {
	def data
	def Mail() {}
	def Mail(data) {
		this.data=data
	}
	def bindData(data)
	{
		this.data=data
	} 
def void sendMail(Closure clo)
{
	def mailResolver=new MailResolver()
	mailResolver.data=this.data
   def trueclo=clo.rehydrate( mailResolver,this, this)
   trueclo.resolveStrategy=Closure.DELEGATE_ONLY
   	 trueclo()
}
}
