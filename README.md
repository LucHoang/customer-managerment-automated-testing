# customer-managerment-automated-testing

Bước 1: bắt đầu làm việc với dự án có sẵn

Truy cập link github sau và clone dự án về: https://github.com/codegym-vn/customer-management

![image](https://user-images.githubusercontent.com/76985354/118076814-1a944100-b3dd-11eb-91bd-ac2b31d73b2d.png)

Sau đó mở dự án bằng IntelIJ và bật cửa sổ terminal lên gõ lệnh git checkout automated-testing và nhấn enter để đổi sang nhánh automated-testing

Sau khi chuyển nhánh thành công, các bạn chọn Load Gradle Project nếu hiển thị popup ở góc dưới bên phải màn hình để dự án được build



Chạy thử chương trình bằng Tomcat

Bước 2: cài đặt unit test framework

Bổ sung các thư viện cần thiết để sử dụng JUnit, những thư viện này sẽ chỉ được compile khi chạy kiểm thử trong dependency file build.gradle

...
testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-engine', version: '5.7.0'
testCompile group: 'org.junit.platform', name: 'junit-platform-commons', version: '1.7.0'
testCompile group: 'org.junit.platform', name: 'junit-platform-launcher', version: '1.7.0'
...
Bổ sung script để gradle chạy test bằng junit platform vào build.gradle:

    test {
    useJUnitPlatform()

    testLogging {
        events "PASSED", "STARTED", "FAILED", "SKIPPED", "STANDARD_OUT", "STANDARD_ERROR"
    }
    afterTest { desc, result ->
        println "Testing ${desc.name} [${desc.className}]: ${result.resultType}"
    }
    reports {
        html.enabled = true
    }
}

Tạo mới một Gradle Configuration cho IntelliJ, với task là "Test":



Thử chạy configuration để chắc chắn không có lỗi xảy ra.

Tạo mới một kiểm thử đơn vị, lưu ý đặt file mã tại thư mục test thay vì source.
Lưu ý: nếu bên trong thư mục test chưa có thư mục con là java thì hãy tạo thêm thư mục java để có thể tạo được các class bên trong. Chuột phải vào thư mục test->New->Directory và gõ java



Tạo lớp DummyTest để kiểm tra các thư viện test xem đã hoạt động chưa sử dụng Annotation @Test để đánh dấu phương thức cần test

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DummyTest {
    @Test
    void assertionWorked() {
        int actual = 1 + 1;
        int expected = 2;
        assertEquals(expected, actual);
    }
}
Chạy lại Test configuration và theo dõi kết quả. Thử thay đổi expected thành 3, thực thi lại và theo dõi kết quả. Nếu kết quả là fail (đúng như dự đoán), chứng tỏ JUnit đã được cài đặt thành công. Hãy xóa dummy test và chuyển sang bước sau.

Bước 3: kết nối JUnit và Spring MVC

Mục tiêu của bước này là khiến kiểm thử sau có thể thực thi được:

package cg.wbd.grandemonstration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void customersListPageIsExists() throws Exception {
        mockMvc
                .perform(get("/customers"))
                .andExpect(status().is(200));
    }
}
Bài kiểm thử trên cố tạo ra một bean controller, giả lập gửi một request tới controller này và xác nhận kết quả hoạt động. Để có thể kiểm thử được, thực hiện những bước cài đặt sau đây.

Bổ sung thư viện spring-tests để có khả năng tích hợp spring mvc với thư viện kiểm thử:

testCompile group: 'org.springframework', name: 'spring-test', version: '5.3.2'
testCompile group: 'com.github.sbrannen', name: 'spring-test-junit5', version: '1.2.0'
Bổ sung repo cho thư viện spring-test-junit5:

repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
Bổ sung thư viện mokito để có khả năng giả lập hành vi:

testCompile group: 'org.mockito', name: 'mockito-all', version: '1.10.19'
file build.gradle sau khi thêm đủ các thư viện như trên sẽ như sau:

plugins {
    id 'java'
    id 'war'
}

group 'cg.wbd'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    providedCompile group: 'javax.servlet', name: 'javax.servlet-api', version: '4.0.1'
    compile group: 'org.springframework', name: 'spring-core', version: '5.3.2'
    compile group: 'org.springframework', name: 'spring-context', version: '5.3.2'
    compile group: 'org.springframework', name: 'spring-beans', version: '5.3.2'
    compile group: 'org.springframework', name: 'spring-web', version: '5.3.2'
    compile group: 'org.springframework', name: 'spring-webmvc', version: '5.3.2'
    compile group: 'org.thymeleaf', name: 'thymeleaf-spring5', version: '3.0.11.RELEASE'
    compile group: 'nz.net.ultraq.thymeleaf', name: 'thymeleaf-layout-dialect', version: '2.5.2'
    compile group: 'org.hibernate', name: 'hibernate-core', version: '5.3.0.Final'
    compile group: 'org.hibernate', name: 'hibernate-entitymanager', version: '5.3.0.Final'
    compile group: 'org.springframework', name: 'spring-orm', version: '5.3.2'
    compile group: 'mysql', name: 'mysql-connector-java', version: '8.0.22'
    compile group: 'org.springframework.data', name: 'spring-data-jpa', version: '2.4.2'
    compile group: 'org.springframework', name: 'spring-tx', version: '5.3.2'
    compile group: 'com.fasterxml.jackson.core', name: 'jackson-databind', version: '2.12.1'
    compile group: 'javax.validation', name: 'validation-api', version: '2.0.1.Final'
    compile group: 'org.hibernate', name: 'hibernate-validator', version: '6.0.10.Final'
    testCompile group: 'org.junit.jupiter', name: 'junit-jupiter-engine', version: '5.7.0'
    testCompile group: 'org.junit.platform', name: 'junit-platform-commons', version: '1.7.0'
    testCompile group: 'org.junit.platform', name: 'junit-platform-launcher', version: '1.7.0'
    testCompile group: 'org.springframework', name: 'spring-test', version: '5.3.2'
    testCompile group: 'com.github.sbrannen', name: 'spring-test-junit5', version: '1.2.0'
    testCompile group: 'org.mockito', name: 'mockito-all', version: '1.10.19'
    testCompile group: 'com.h2database', name: 'h2', version: '1.4.197'
}

test {
    useJUnitPlatform()

    testLogging {
        events "PASSED", "STARTED", "FAILED", "SKIPPED", "STANDARD_OUT", "STANDARD_ERROR"
    }
    afterTest { desc, result ->
        println "Testing ${desc.name} [${desc.className}]: ${result.resultType}"
    }
    reports {
        html.enabled = true
    }
}
Chạy thử kiểm thử. Tới đây kiểm thử đã có thể compile, nhưng khi thực thi, controller đang thiếu thành phần cần thiết để có thể khởi tạo (là bean service). Để đảm bảo sự cô lập khi kiểm thử controller, (các) bean này sẽ không được tạo thật mà được "mock" (giả tạo). Mock bean này bằng cách tạo một class cấu hình test:

@WebAppConfiguration
public class CustomerControllerTest {
    private CustomerService customerService = Mockito.mock(CustomerServiceImplWithSpringData.class);
Để mock service có thể được tạo ra, sự hiện diện của một entity manager là cần thiết (do service có sử dụng đến repository bean). Có thể mock bean này bằng cách khai báo một mocked datasource và để spring test lo phần còn lại.

Bổ sung thư viện h2 database để có được nhanh nhất một datasource:

testCompile group: 'com.h2database', name: 'h2', version: '1.4.197'
Tạo một lớp để cấu hình test config và chúng ta thêm bean datasource vào đây:

package cg.wbd.grandemonstration.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@ComponentScan("cg.wbd.grandemonstration")
public class CustomerControllerTestConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("cms")
                .build();
    }

}
Và bổ sung annotation @SpringJUnitJupiterConfig lớp CustomerControllerTest
@WebAppConfiguration
@SpringJUnitJupiterConfig(CustomerControllerTestConfig.class)
public class CustomerControllerTest {

…

}
Cấu trúc thư mục lúc này:



Chạy thử kiểm thử, đọc lỗi, và tiếp tục bổ sung mock service còn thiếu trong file CustomerControllerTest:

private ProvinceService provinceService = Mockito.mock(ProvinceServiceImplWithSpringData.class);
Chạy thử kiểm thử, nhận thấy rằng các mocked service đã hoạt động, request giả lập đã được gửi nhưng context test đang không phẩn giải đối tượng pageable cho controller - điều mà context chạy thật không gặp phải (nhờ annotation "@EnableSpringDataWebSupport"). Để tái hiện điều này trên test context, bổ sung tham số vào mockMvc builder:

@BeforeEach
void setUp() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders
            .standaloneSetup(customerController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
}
Chạy kiểm thử, xác nhận rằng kiểm thử đã PASS.

Có thể thay is(200) bằng isOk():

.andExpect(status().is(200));
Bước 4: kiểm thử controller

Bổ sung kiểm thử về view name của màn hình duyệt danh sách khách hàng:

@Test
void customerBrowseControlling() throws Exception {
    mockMvc
            .perform(get("/customers"))
            .andExpect(status().is(200))
            .andExpect(view().name("customers/browse"));
}
Chạy test để thấy kiểm thử đã failed, để vượt qua kiểm thử, đổi tên view:

@GetMapping
public ModelAndView showList(Optional<String> s, Pageable pageInfo) {
    ModelAndView modelAndView = new ModelAndView("customers/browse");
    Page<Customer> customers = s.isPresent() ? search(s, pageInfo) : getPage(pageInfo);
    modelAndView.addObject("keyword", s.orElse(null));
    modelAndView.addObject("customers", customers);
    return modelAndView;
}
Bổ sung kiểm thử cho phương thức showInformation() như sau

@Test
void showInformationSuccessControlling() throws Exception {
    Customer customer = new Customer(1L, "Foo Bar", "a@dummy.im", "Nowhere");
    given(customerService.findOne(1L)).willReturn(Optional.of(customer));
    mockMvc
            .perform(get("/customers/1"))
            .andExpect(status().is(200))
            .andExpect(view().name("customers/info"));
}
Bổ sung một kiểm thử khác để kiểm thử cho request update:

@Test
void customerUpdateSuccessControlling() throws Exception {
    Customer customer = new Customer(1L, "Foo Bar", "a@dummy.im", "Nowhere");
    when(customerService.save(isA(Customer.class))).thenReturn(customer);
    mockMvc
            .perform(post("/customers")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("id", customer.getId().toString())
                    .param("name", customer.getName())
                    .param("email", customer.getEmail())
                    .param("address", customer.getAddress()))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/customers"));
}
Kiểm thử này giả định trước hành vi của service sẽ là success. Sau đó gửi giả lập một request cập nhật customer. Yêu cầu controller phải trả về redirect tới màn hình duyệt danh sách.

Chạy thử để thấy kiểm thử đã pass.

Bạn có thể tiếp tục bổ sung các kiểm thử controller khác.

Bước 5: tạo kiểm thử service

Tạo bài test cho customer service. Do service sẽ triệu gọi repository nên sẽ cần mock repository.

package cg.wbd.grandemonstration.service;


import cg.wbd.grandemonstration.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitJupiterConfig;


@SpringJUnitJupiterConfig(CustomerServiceTestConfig.class)
public class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    private void resetMocks() {
        Mockito.reset(customerRepository);
    }
}
Provide các đối tượng phụ thuộc:

package cg.wbd.grandemonstration.service;

import cg.wbd.grandemonstration.repository.CustomerRepository;
import cg.wbd.grandemonstration.service.impl.CustomerServiceImplWithSpringData;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerServiceTestConfig {
    @Bean
    public CustomerService customerService() {
        return new CustomerServiceImplWithSpringData();
    }
    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }
}
Bước 6: Bổ sung kiểm thử service

 Kiểm thử này giả lập hành vi cho repository, sau đó xác nhận rằng khi findAll của service được gọi thì findAll của repository được gọi, cũng như xác nhận rằng kết quả là chính xác.

@Test
void testFindAll() {
    List<Customer> customers = new ArrayList<>();
    customers.add(new Customer(1L, "Foo Bar", "a@dummy.im", "Nowhere"));
    Pageable pageInfo = PageRequest.of(0, 25);
    Page<Customer> customerPage = new PageImpl<Customer>(customers, pageInfo, 1);
    when(customerRepository.findAll(pageInfo)).thenReturn(customerPage);

    Page<Customer> actual = customerService.findAll(pageInfo);
    verify(customerRepository).findAll(pageInfo);
    assertEquals(customerPage, actual);
}
Kiểm thử phương thức findById khi tìm thấy được customer tương ứng với id truyền vào

@Test
void testFindByIdFound() {
    Customer customer = new Customer(1L, "Foo Bar", "a@dummy.im", "Nowhere");
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    Optional<Customer> actual = customerService.findOne(1L);
    verify(customerRepository).findById(1L);
    assertEquals(Optional.of(customer), actual);
}

Kiểm thử phương thức findById khi không tìm thấy customer tương ứng với id truyền vào (do findById trả về kiểu Optional nên để so sánh giá trị rỗng ta dùng hàm Optional.empty())

@Test
void testFindByIdNotFound() {
    given(customerRepository.findById(1L)).willReturn(Optional.empty());

    Optional<Customer> actual = customerService.findOne(1L);
    verify(customerRepository).findById(1L);
    assertEquals(Optional.empty(), actual);
}
Kiểm thử phương thức tạo mới một customer

@Test
void saveCustomer() {
    Customer customer = new Customer(1L, "Foo Bar", "a@dummy.im", "Nowhere");
    customerService.save(customer);
    verify(customerRepository).save(customer);
}
Tương tự, những kiểm thử này giả lập hành vi cho repository, sau đó xác nhận hành vi của service.
