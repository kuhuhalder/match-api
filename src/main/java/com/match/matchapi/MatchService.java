package com.match.matchapi;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for our web app, contains all methods
 * for the API's
 *
 * @author Prince Rawal
 * @author Farah Lubaba Rouf
 */

@Service
@AllArgsConstructor
public class MatchService
{

    /**
     * Constants for sending and receiving match requests
     */

    private static final int BADREQUEST = -1;
    private static final int REQUESTSENT = 0;
    private static final int REQUESTSENTANDMATCHED = 1;
    private static final int REQUESTEXISTS = 2;
    private static final int ALREADYMATCHED = 3;
    private static final int NOUSERREQUESTED = -1;
    private static final int USER1REQUESTEDUSER2 = 0;
    private static final int USER2REQUESTEDUSER1 = 1;
    private static final int MATCHED = 2;
    private static final int ALERTREQUEST = 0;
    private static final int ALERTMATCH = 1;
    private static final int ALERTREQUESTSENT = 2;
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final CourseRepository courseRepository;
    @Autowired
    private final MatchRepository matchRepository;
    @Autowired
    private final MongoTemplate mongoTemplate;

    /**
     * Gets list of all students in database
     *
     * @return list of all students, excluding admin
     */

    public List<Student> getAllStudents()
    {
        Query nonAdminQuery = new Query();
        nonAdminQuery.addCriteria(
                new Criteria().orOperator(
                        Criteria.where("isAdmin").isNull(),
                        Criteria.where("isAdmin").is(0)
                )
        );
        return mongoTemplate.find(nonAdminQuery, Student.class);
    }

    /**
     * Checks that a student exists in the database
     *
     * @param username is username of student
     * @param password is account password
     * @return true if account exists, false otherwise
     */

    public boolean validation(String username, String password)
    {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("userName").is(username),
                Criteria.where("password").is(password)
        ));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.isEmpty())
        {
            return false;
        }

        return students.size() == 1;
    }

    /**
     * Checks that student exists given only username
     *
     * @param username is student's username
     * @return true if account exists, false otherwise
     */

    public boolean studentExists(String username)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(username));

        List<Student> students = mongoTemplate.find(query, Student.class);

        return (students != null) && (students.size() == 1);
    }

    /**
     * Given student object, adds a student to the database
     *
     * @param student is a Student object with all the info
     * @return true if student doesn't exist and is added, false otherwise
     */

    public boolean addStudent(Student student)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(student.getUserName()));
        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 0)
        {
            return false;
        }

        if (student.getIsAdmin() == null)
        {
            student.setIsAdmin(0);
        }
        studentRepository.insert(student);
        return true;
    }

    /**
     * Deletes a student from the databasem, deletes all matches
     * of the student as well
     *
     * @param userName is student username
     * @return true if student exists and is able to be deleted,
     * false otherwise
     */

    public boolean deleteStudent(String userName)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(userName));
        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students == null || students.size() == 0)
        {
            return false;
        }

        Query deleteMatchQuery = new Query();
        deleteMatchQuery.addCriteria(new Criteria().orOperator(
                Criteria.where("userOneId").is(userName),
                Criteria.where("userTwoId").is(userName)
        ));

        List<Matches> matchesToDelete = mongoTemplate.find(deleteMatchQuery, Matches.class);
        if (matchesToDelete != null)
        {
            for (int i = 0; i < matchesToDelete.size(); i++)
            {
                matchRepository.deleteById(matchesToDelete.get(i).getId());
            }
        }

        studentRepository.deleteById(students.get(0).getId());
        return true;

    }

    /**
     * Given a username finds a student in the database
     *
     * @param userName is student username
     * @return matching student object
     */

    public Student getStudent(String userName)
    {
        return mongoTemplate.findById(userName, Student.class);
    }

    /**
     * Updates student object in database
     *
     * @param student is student object
     * @return true if student is updated, false if it doesn't exist
     */

    public boolean updateStudent(Student student)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(student.getId()));
        List<Student> students = mongoTemplate.find(query, Student.class);
        if (students == null || students.size() <= 0)
        {
            return false;
        }

        student.setUserName(students.get(0).getUserName());

        if (student.getCourse() == null)
        {
            student.setCourse(students.get(0).getCourse());
        }

        if (student.getCampus() == null)
        {
            student.setCampus(students.get(0).getCampus());
        }
        if (student.getFirstName() == null)
        {
            student.setFirstName(students.get(0).getFirstName());
        }
        if (student.getLastName() == null)
        {
            student.setLastName(students.get(0).getLastName());
        }
        if (student.getBio() == null)
        {
            student.setBio(students.get(0).getBio());
        }
        if (student.getMajor() == null)
        {
            student.setMajor(students.get(0).getMajor());
        }
        if (student.getPronouns() == null)
        {
            student.setPronouns(students.get(0).getPronouns());
        }
        if (student.getPassword() == null)
        {
            student.setPassword(students.get(0).getPassword());
        }
        if (student.getYear() == null)
        {
            student.setYear(students.get(0).getYear());
        }
        if (student.getGenderPreference() == null)
        {
            student.setGenderPreference(students.get(0).getGenderPreference());
        }
        if (student.getContactInfo() == null)
        {
            student.setContactInfo(students.get(0).getContactInfo());
        }

        studentRepository.deleteById(student.getId());
        studentRepository.insert(student);
        return true;

    }

    /**
     * Helper method for mergesorting the points for similarity index
     *
     * @param points   is the points each student has
     * @param students is the list of all students
     */

    private void merge(List<Integer> points, List<Student> students, int l, int m, int r)
    {


        /* Create temp arrays */
        int[] Lpoints = new int[m - l + 3];
        int[] Rpoints = new int[r - m + 3];

        Student[] Lstudents = new Student[m - l + 3];
        Student[] Rstudents = new Student[r - m + 3];

        /*Copy data to temp arrays*/
        for (int i = l; i <= m; i++)
        {
            Lpoints[i - l] = points.get(i);
            Lstudents[i - l] = students.get(i);
        }
        for (int j = m + 1; j <= r; j++)
        {
            Rpoints[j - (m + 1)] = points.get(j);
            Rstudents[j - (m + 1)] = students.get(j);
        }

        int i = l, j = m + 1;

        int k = l;
        while (i <= m && j <= r)
        {
            if (Lpoints[i - l] >= Rpoints[j - (m + 1)])
            {
                points.set(k, Lpoints[i - l]);
                students.set(k, Lstudents[i - l]);
                i++;
                k++;
            }
            else
            {
                points.set(k, Rpoints[j - (m + 1)]);
                students.set(k, Rstudents[j - (m + 1)]);
                j++;
                k++;
            }
        }

        /* Copy remaining elements of L[] if any */
        while (i <= m)
        {
            points.set(k, Lpoints[i - l]);
            students.set(k, Lstudents[i - l]);
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j <= l)
        {
            points.set(k, Rpoints[j - (m + 1)]);
            students.set(k, Rstudents[j - (m + 1)]);
            j++;
            k++;
        }
    }

    /**
     * Helper method for mergesorting the points for similarity index
     *
     * @param points   is the points each student has
     * @param students is the list of all students
     */

    void sort(List<Integer> points, List<Student> students, int l, int r)
    {
        if (r <= l)
        {
            return;
        }
        if (l < r)
        {
            // Find the middle point
            int m = (r + l) / 2;

            // Sort first and second halves
            sort(points, students, l, m);
            sort(points, students, m + 1, r);

            // Merge the sorted halves
            merge(points, students, l, m, r);
        }
    }

    /**
     * Our main business logic, which returns a list of best matches for the
     * student in descending order. Points are given based on course,campus,
     * gender preference, and major
     *
     * @param student1 is the student we are finding matches for
     * @param student2 is the student we are comparing to and giving points to
     * @return points for each student
     */

    private Integer similarityIndex(Student student1, Student student2)
    {
        int points = 0;
        if (student1.getMajor() != null && student2.getMajor() != null)
        {
            if (student1.getMajor().equals(student2.getMajor()))
            {
                points += 2;
            }
        }

        if (student1.getYear() != null && student2.getYear() != null)
        {
            if (student1.getYear().equals(student2.getYear()))
            {
                points += 1;
            }
        }

        if (student1.getCampus() != null && student2.getCampus() != null)
        {
            if (student1.getCampus().equals(student2.getCampus()))
            {
                points += 2;
            }
        }

        if (student1.getGenderPreference() != null && student2.getGenderPreference() != null)
        {
            if (student1.getGenderPreference().equals(student2.getGenderPreference()))
            {
                points += 2;
            }
        }

        if (student1.getCourse() != null && student2.getCourse() != null)
        {
            for (int i = 0; i < student1.getCourse().size(); i++)
            {
                for (int j = 0; j < student2.getCourse().size(); j++)
                {
                    if (student1.getCourse().get(i).equals(student2.getCourse().get(j)))
                    {
                        points += 3;
                        break;
                    }
                }
            }
        }

        return points;

    }

    /**
     * Applies similarity index algorithm on all the students
     * to display the list of potential matches in
     * descending order of points
     *
     * @param userName is the user of the student we are finding
     *                 matches for
     * @return list of students in descending order
     */

    public List<Student> findBuddies(String userName)
    {

        Student student = getStudent(userName);
        if (student == null)
        {
            return null;
        }
        //list of all students

        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("admin").isNull(),
                Criteria.where("admin").is(0)
        ));

        List<Student> allStudents = mongoTemplate.find(query, Student.class);

        List<Student> studentBuddies = findConfirmedMatches(userName);
        List<Student> studentsAlreadyRequested = findRequestsSent(userName);

        List<Student> filteredStudents = new ArrayList<>();

        int breakIndicator = 0;
        for (int i = 0; i < allStudents.size(); i++)
        {
            breakIndicator = 0;
            for (int j = 0; j < studentBuddies.size(); j++)
            {
                if (allStudents.get(i).getUserName().equals(studentBuddies.get(j).getUserName()))
                {
                    breakIndicator = 1;
                    break;
                }
            }
            for (int j = 0; j < studentsAlreadyRequested.size(); j++)
            {
                if (allStudents.get(i).getUserName().equals(studentsAlreadyRequested.get(j).getUserName()))
                {
                    breakIndicator = 1;
                    break;
                }
            }
            if (breakIndicator == 0)
            {
                filteredStudents.add(allStudents.get(i));
            }
        }

        allStudents = filteredStudents;

        //points array
        List<Integer> points = new ArrayList<>();

        for (int i = 0; i < allStudents.size(); i++)
        {
            if (allStudents.get(i).getUserName().equals(student.getUserName()))
            {
                allStudents.remove(i);
                break;
            }
        }

        for (int i = 0; i < allStudents.size(); i++)
        {
            points.add(similarityIndex(student, allStudents.get(i)));
        }

        sort(points, allStudents, 0, allStudents.size() - 1);

        return allStudents;
    }

    /**
     * Checks that match exists between 2 students
     *
     * @param userName1,username2 the users of the 2 students
     * @return constant based on who requested whom or if there were
     * no requests sent
     */


    public int matchExists(String userName1, String userName2)
    {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").is(userName1 + "+" + userName2));

        List<Matches> matches1 = mongoTemplate.find(query1, Matches.class);

        Query query2 = new Query();
        query2.addCriteria(Criteria.where("id").is(userName2 + "+" + userName1));

        List<Matches> matches2 = mongoTemplate.find(query2, Matches.class);

        if ((matches1 != null) && (matches2 != null) &&
                (matches1.size() > 0) && (matches2.size() > 0))
        {
            return MATCHED;
        }

        if ((matches1 != null) && (matches1.size() > 0))
        {
            return USER1REQUESTEDUSER2;
        }

        if ((matches1 != null) && (matches1.size() > 0))
        {
            return USER2REQUESTEDUSER1;
        }

        return NOUSERREQUESTED;
    }

    /**
     * Adds a match to the database
     *
     * @param match is the match to be added to the database
     * @return an integer based on whether it's a valid match
     * or not
     */

    public int addMatches(Matches match)
    {

        if (match.getId() == null || match.getUserOneId() == null || match.getUserTwoId() == null)
        {
            return BADREQUEST;
        }

        if (!match.getId().equals(match.getUserOneId() + "+" + match.getUserTwoId()))
        {
            return BADREQUEST;
        }


        if (!(studentExists(match.getUserOneId()) && studentExists(match.getUserTwoId())))
        {
            return BADREQUEST;
        }

        int matchExistsVar = matchExists(match.getUserOneId(), match.getUserTwoId());

        if (matchExistsVar == MATCHED)
        {
            return ALREADYMATCHED;
        }

        if (matchExistsVar == USER1REQUESTEDUSER2)
        {
            return REQUESTEXISTS;
        }

        matchRepository.insert(match);
        return matchExistsVar == USER2REQUESTEDUSER1 ? REQUESTSENTANDMATCHED : REQUESTSENT;
    }

    /**
     * Deletes a match from the database
     *
     * @param matchId is the match id to be deleted
     * @return true or false depending on whether match can be deleted
     */

    public boolean deleteMatch(String matchId)
    {
        System.out.println(matchId);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(matchId));
        List<Matches> matches = mongoTemplate.find(query, Matches.class);
        if (matches == null || matches.size() == 0)
        {
            return false;
        }
        matchRepository.deleteById(matches.get(0).getId());
        return true;

    }


    /**
     * If two users sent match requests to each other, makes them
     * study buddies and retuns euther the confirmed (matched)
     * buddies or the requests sent
     *
     * @param userName is the student user
     * @return list of students who have matched with user or
     * sent them a request
     */


    public List<Student> alertsHelper(String userName, int indicator)
    {
        if (!studentExists(userName))
        {
            return null;
        }

        Query queryReceived = new Query();
        queryReceived.addCriteria(Criteria.where("userTwoId").is(userName));

        List<Matches> requestsReceived = mongoTemplate.find(queryReceived, Matches.class);

        Query querySent = new Query();
        querySent.addCriteria(Criteria.where("userOneId").is(userName));

        List<Matches> requestsSent = mongoTemplate.find(querySent, Matches.class);

        List<Student> buddies = new ArrayList<>();

        for (int i = 0; i < requestsSent.size(); i++)
        {
            for (int j = 0; j < requestsReceived.size(); j++)
            {
                if (requestsReceived.get(j).getUserOneId().equals(requestsSent.get(i).getUserTwoId()))
                {
                    buddies.add(getStudent(requestsReceived.get(j).getUserOneId()));
                    break;
                }
            }
        }

        if (indicator == ALERTMATCH)
        {
            return buddies;
        }

        List<Student> requests = new ArrayList<>();

        if (indicator == ALERTREQUESTSENT)
        {

            int breakIndicator = 0;
            for (int i = 0; i < requestsSent.size(); i++)
            {
                breakIndicator = 0;
                for (int j = 0; j < buddies.size(); j++)
                {
                    if (buddies.get(j).getUserName().equals(requestsSent.get(i).getUserTwoId()))
                    {
                        breakIndicator = 1;
                        break;
                    }
                }
                if (breakIndicator == 1)
                {
                    continue;
                }
                requests.add(getStudent(requestsSent.get(i).getUserTwoId()));
            }
            return requests;
        }
        else
        {

            int breakIndicator = 0;
            for (int i = 0; i < requestsReceived.size(); i++)
            {
                breakIndicator = 0;
                for (int j = 0; j < buddies.size(); j++)
                {
                    if (buddies.get(j).getUserName().equals(requestsReceived.get(i).getUserOneId()))
                    {
                        breakIndicator = 1;
                        break;
                    }
                }
                if (breakIndicator == 1)
                {
                    continue;
                }
                requests.add(getStudent(requestsReceived.get(i).getUserOneId()));
            }
            return requests;
        }


    }

    /**
     * Find the requests the student has received
     *
     * @param username is the user of the student
     * @return list of students who have sent requests to user
     */

    public List<Student> findRequests(String username)
    {
        return alertsHelper(username, ALERTREQUEST);
    }

    /**
     * Find the study buddies for the students (if both
     * have sent matches to each other)
     *
     * @param username is the user of the student
     * @return list of students who are study buddies with
     * the student
     */

    public List<Student> findConfirmedMatches(String username)
    {
        return alertsHelper(username, ALERTMATCH);
    }

    /**
     * Find the requests student has sent
     *
     * @param username is the user of the student
     * @return list of students who the user sent requests to
     */

    public List<Student> findRequestsSent(String username)
    {
        return alertsHelper(username, ALERTREQUESTSENT);
    }

    /**
     * Allows admin to add a course to the database
     *
     * @param course is the course to be added
     * @return true if course is able to be added,
     * false otherwise
     */

    public boolean addCourse(Course course)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(course.getId()));
        List<Course> courses = mongoTemplate.find(query, Course.class);

        if (courses.size() > 0)
        {
            return false;
        }
        courseRepository.insert(course);
        return true;
    }

    /**
     * Gets all courses from course repository
     *
     * @return list of all courses
     */

    public List<Course> getAllCourses()
    {
        return courseRepository.findAll();
    }

    /**
     * Gets course by id
     *
     * @param id of course
     * @return the course
     */

    public Course getCourseById(String id)
    {
        return mongoTemplate.findById(id, Course.class);
    }

    /**
     * Deletes courses from course repository
     *
     * @param id of course to be deleted
     */

    public boolean deleteCourse(String id)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        List<Course> courses = mongoTemplate.find(query, Course.class);
        if (courses == null || courses.size() == 0)
        {
            return false;
        }

        courseRepository.deleteById(courses.get(0).getId());
        return true;

    }

    /**
     * Gets all confirmed matches for admin
     *
     * @return list of all confirmed matches
     */

    public List<Matches> getAllConfirmedMatches()
    {
        List<Matches> allMatches = matchRepository.findAll();
        List<Matches> confirmedMatches = new ArrayList<>();
        int[] indicator = new int[allMatches.size()];
        for (int i = 0; i < allMatches.size(); i++)
        {
            if (indicator[i] != 1)
            {
                for (int j = i + 1; j < allMatches.size(); j++)
                {
                    if (allMatches.get(i).getUserOneId().equals(allMatches.get(j).getUserTwoId()) &&
                            allMatches.get(i).getUserTwoId().equals(allMatches.get(j).getUserOneId()))
                    {
                        indicator[i] = 1;
                        indicator[j] = 1;
                        confirmedMatches.add(allMatches.get(i));
                        break;
                    }
                }
            }
        }
        return confirmedMatches;
    }


}
