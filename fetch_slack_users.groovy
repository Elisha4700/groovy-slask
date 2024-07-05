import groovy.json.JsonSlurper



def fetchAllMemebers(slackToken) {
  def filteredMembers = []
  
  try {
    def url = "https://slack.com/api/users.list?token=${slackToken}"

    def response = new URL(url).text
    def jsonSlurper = new JsonSlurper()
    def jsonResponse = jsonSlurper.parseText(response)

    //def count = 1
    if (jsonResponse.ok == true) {
      println "Yeah, we've managed to get a good response"

      jsonResponse.members.each { member ->
        if (!member.deleted && !member.is_bot) {
          //println "(${count}): ${member.name}  ->  ${member.id}"
          //count += 1
          filteredMembers << ["name": member.name, "id": member.id]
        }
      }
    } else {
      println "Somewhing went wrong"
    }

  } catch (Exception err) {
    println "ERROR :: while fetching slack users: ${err.message}"
  }


  return filteredMembers
}


def findUserIdByName(userName) {
  userName = userName.trim().toLowerCase().replaceAll(" ", ".")
  
  def allMembers = fetchAllMemebers()

  def user = allMembers.find { it.name == userName }

  if (user) {
    return user.id
  }
}

def getUsersByName(name) {
  userId = findUserIdByName(name) 

  if (userId) {
    println "The userId for: ${name} is: '${userId}'"
  } else {
    println "User with name '${name}' not found"
  }
}

getUsersByName("Elisha Shapiro")
