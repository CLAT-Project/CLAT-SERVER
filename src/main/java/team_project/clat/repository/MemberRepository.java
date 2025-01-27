package team_project.clat.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team_project.clat.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByUsername(String username);
    Member findByUsername(String username);
    void deleteByUsername(String username);

    Member findByName(String name);

    @Query("select distinct m from Member m left join fetch m.messageList where m.id = :memberId")
    Optional<Member> findByMemberId(@Param("memberId") Long memberId);
}
