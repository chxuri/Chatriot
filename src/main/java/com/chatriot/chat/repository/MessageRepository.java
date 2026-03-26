//finds by class period
public interface MessageRepository extends MongoRepository<ChatMessage, String> 
{
    List<ChatMessage> findByClassId(String classId);
}