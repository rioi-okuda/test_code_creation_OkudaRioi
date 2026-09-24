package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:8080/lms/");
		assertEquals("ログイン | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//id入力
		WebElement idElement = webDriver.findElement(By.id("loginId"));
		idElement.clear();
		idElement.sendKeys("StudentAA01");

		//パスワード入力
		WebElement passElement = webDriver.findElement(By.id("password"));
		passElement.clear();
		passElement.sendKeys("StudentAA01");

		getEvidence(new Object() {
		}, "01");

		//ログインボタン押下
		webDriver.findElement(By.cssSelector("input[value='ログイン']")).click();

		pageLoadTimeout(20);

		//タイトルチェック
		assertEquals("コース詳細 | LMS", webDriver.getTitle());

		//URLチェック
		assertEquals("http://localhost:8080/lms/course/detail", webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "02");

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		webDriver.findElement(By.cssSelector("li.dropdown .dropdown-toggle")).click();
		visibilityTimeout(By.partialLinkText("ヘルプ"), 10);

		getEvidence(new Object() {
		}, "01");

		webDriver.findElement(By.linkText("ヘルプ")).click();

		//タイトルチェック
		assertEquals("ヘルプ | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		String originalWindow = webDriver.getWindowHandle();

		webDriver.findElement(By.linkText("よくある質問")).click();

		pageLoadTimeout(20);

		for (String windowHandle : webDriver.getWindowHandles()) {
			if (!originalWindow.contentEquals(windowHandle)) {
				webDriver.switchTo().window(windowHandle);
				break;
			}
		}

		//タイトルチェック
		assertEquals("よくある質問 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		//検索テキストボックスにキャンセルと入力
		webDriver.findElement(By.id("form")).sendKeys("キャンセル");

		getEvidence(new Object() {
		}, "01");

		//検索ボタン押下
		webDriver.findElement(By.cssSelector("input[value='検索']")).click();

		scrollTo("100");

		//検索質問をすべて取得
		List<WebElement> faqList = webDriver.findElements(By.cssSelector("dt.mb10"));

		//キャンセルが含まれるか検索
		for (WebElement element : faqList) {
			assertThat(element.getText(), containsString("キャンセル"));
		}

		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		scrollTo("-100");

		getEvidence(new Object() {
		}, "01");

		//検索ボタン押下
		webDriver.findElement(By.cssSelector("input[value='クリア']")).click();

		//検索テキストボックスが空なら
		assertEquals("", webDriver.findElement(By.id("form")).getText());

		getEvidence(new Object() {
		}, "02");
	}

}
