<script setup>
import { ref, nextTick } from 'vue'

const isOpen = ref(false)
const input = ref('')
const messagesEnd = ref(null)
const messages = ref([
  {
    role: 'bot',
    content: '안녕하세요! Logitech 쇼핑몰 챗봇입니다 \n궁금한 점을 물어보세요!\n\n아래 버튼을 눌러 빠르게 질문할 수 있어요!'
  }
])

// FAQ 데이터
const faqs = [
  {
    keywords: ['배송', '언제', '며칠', '배달'],
    answer: '📦 배송 안내\n주문 완료 후 2-3일 이내 배송됩니다.\n평일 오후 2시 이전 주문 시 당일 출고됩니다.'
  },
  {
    keywords: ['반품', '교환', '환불', '취소'],
    answer: '🔄 반품/교환 안내\n- 단순변심: 수령 후 7일 이내 가능\n- 불량품: 수령 후 30일 이내 가능\n주문취소는 주문내역 페이지에서 직접 가능합니다.'
  },
  {
    keywords: ['결제', '카드', '무통장', '계좌', '입금'],
    answer: '💳 결제 안내\n- 신용카드\n- 무통장입금: 한국은행 123-45678-90\n무통장입금 시 주문 후 24시간 이내 입금해주세요.'
  },
  {
    keywords: ['회원가입', '가입', '등록'],
    answer: '👤 회원가입 안내\n회원가입은 무료입니다!\n상단 메뉴에서 회원가입 버튼을 클릭해주세요.'
  },
  {
    keywords: ['로그인', '비밀번호', '아이디'],
    answer: '🔐 로그인 안내\n상단 메뉴에서 로그인 버튼을 클릭해주세요.\n비밀번호를 잊으셨다면 Q&A 게시판에 문의해주세요.'
  },
  {
    keywords: ['장바구니', '담기', '추가'],
    answer: '🛒 장바구니 안내\n상품 페이지에서 장바구니 담기 버튼을 클릭하세요.\n로그인 후 이용 가능합니다.'
  },
  {
    keywords: ['주문', '구매', '결제하기'],
    answer: '📋 주문 안내\n장바구니에서 주문하기 버튼을 클릭하거나\n상품 상세 페이지에서 바로 주문 가능합니다.'
  },
  {
    keywords: ['주문내역', '주문확인', '주문조회'],
    answer: '📋 주문내역 조회\n상단 메뉴의 주문내역에서 확인 가능합니다.\n주문 취소도 해당 페이지에서 가능합니다.'
  },
  {
    keywords: ['리뷰', '후기', '평점'],
    answer: '⭐ 리뷰 안내\n구매 완료 후 주문 상세 페이지에서 리뷰 작성이 가능합니다.'
  },
  {
    keywords: ['qna', 'q&a', '질문', '문의', '고객센터'],
    answer: '💬 Q&A 안내\n상단 메뉴의 Q&A 게시판에서 문의 가능합니다.\n고객센터 운영시간: 평일 09:00-18:00'
  },
  {
    keywords: ['마우스', '키보드', '상품', '제품'],
    answer: '🖱️ 상품 안내\n메인 페이지에서 다양한 로지텍 제품을 확인하세요!\n신상품, 베스트, 추천 상품을 한눈에 볼 수 있습니다.'
  },
  {
    keywords: ['재고', '품절', '없음'],
    answer: '📦 재고 안내\n상품 상세 페이지에서 재고 현황을 확인하세요.\n품절 상품은 Q&A 게시판에 문의해주세요.'
  }
]

// 빠른 질문 버튼
const quickQuestions = [
  '배송은 얼마나 걸리나요?',
  '반품/교환 방법이 궁금해요',
  '결제 수단이 어떻게 되나요?',
  '주문 취소하고 싶어요',
  '리뷰는 어떻게 쓰나요?'
]

const scrollToBottom = async () => {
  await nextTick()
  messagesEnd.value?.scrollIntoView({ behavior: 'smooth' })
}

const getAnswer = (text) => {
  const lowerText = text.toLowerCase()
  for (const faq of faqs) {
    if (faq.keywords.some(keyword => lowerText.includes(keyword))) {
      return faq.answer
    }
  }
  return '죄송해요, 해당 질문에 대한 답변을 찾지 못했습니다 \nQ&A 게시판에 문의해주시면 빠르게 답변드리겠습니다!'
}

const sendMessage = async (text = null) => {
  const userInput = text || input.value.trim()
  if (!userInput) return

  input.value = ''
  messages.value.push({ role: 'user', content: userInput })
  await scrollToBottom()

  // 답변 딜레이 (자연스러운 느낌)
  setTimeout(async () => {
    messages.value.push({ role: 'bot', content: getAnswer(userInput) })
    await scrollToBottom()
  }, 500)
}

const handleKeydown = (e) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

const clearMessages = () => {
  messages.value = [
    {
      role: 'bot',
      content: '안녕하세요! Logitech 쇼핑몰 챗봇입니다 \n궁금한 점을 물어보세요!\n\n아래 버튼을 눌러 빠르게 질문할 수 있어요!'
    }
  ]
}
</script>

<template>
  <div class="chatbot-wrapper">
    <!-- 토글 버튼 -->
    <button class="chatbot-toggle btn btn-primary rounded-circle shadow"
      @click="isOpen = !isOpen">
      <i :class="isOpen ? 'bi bi-x-lg' : 'bi bi-chat-dots-fill'" class="fs-5"></i>
    </button>

    <!-- 챗봇 창 -->
    <transition name="slide-up">
      <div v-if="isOpen" class="chatbot-window card shadow-lg">

        <!-- 헤더 -->
        <div class="chatbot-header bg-primary text-white p-3 d-flex justify-content-between align-items-center">
          <div class="d-flex align-items-center gap-2">
            <i class="bi bi-robot fs-5"></i>
            <span class="fw-bold">Logitech 챗봇</span>
            <span class="badge bg-success" style="font-size: 10px">온라인</span>
          </div>
          <button class="btn btn-sm btn-outline-light" @click="clearMessages" title="대화 초기화">
            <i class="bi bi-arrow-clockwise"></i>
          </button>
        </div>

        <!-- 메시지 목록 -->
        <div class="chatbot-messages p-3">
          <div v-for="(msg, idx) in messages" :key="idx"
            :class="['message-wrapper', msg.role === 'user' ? 'user' : 'bot']">
            <div :class="['message-bubble', msg.role === 'user' ? 'user-bubble' : 'bot-bubble']">
              <pre class="mb-0" style="white-space: pre-wrap; font-family: inherit; font-size: 0.85rem">{{ msg.content }}</pre>
            </div>
          </div>
          <div ref="messagesEnd"></div>
        </div>

        <!-- 빠른 질문 버튼 -->
        <div class="quick-questions px-3 pb-2">
          <div class="d-flex flex-wrap gap-1">
            <button v-for="q in quickQuestions" :key="q"
              class="btn btn-sm btn-outline-primary"
              style="font-size: 0.75rem"
              @click="sendMessage(q)">
              {{ q }}
            </button>
          </div>
        </div>

        <!-- 입력창 -->
        <div class="chatbot-input p-3 border-top">
          <div class="input-group">
            <textarea v-model="input" class="form-control"
              placeholder="메시지를 입력하세요..." rows="2"
              style="resize: none; font-size: 0.875rem"
              @keydown="handleKeydown">
            </textarea>
            <button class="btn btn-primary" @click="sendMessage()">
              <i class="bi bi-send-fill"></i>
            </button>
          </div>
          <small class="text-muted mt-1 d-block">Enter로 전송, Shift+Enter로 줄바꿈</small>
        </div>

      </div>
    </transition>
  </div>
</template>

<style scoped>
.chatbot-wrapper {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 9999;
}

.chatbot-toggle {
  width: 60px;
  height: 60px;
  border-radius: 50% !important;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chatbot-window {
  position: absolute;
  bottom: 75px;
  right: 0;
  width: 370px;
  height: 520px;
  display: flex;
  flex-direction: column;
  border-radius: 16px !important;
  overflow: hidden;
}

.chatbot-header {
  flex-shrink: 0;
}

.chatbot-messages {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  background: #f8f9fa;
}

.message-wrapper {
  display: flex;
}

.message-wrapper.user {
  justify-content: flex-end;
}

.message-wrapper.bot {
  justify-content: flex-start;
}

.message-bubble {
  max-width: 82%;
  padding: 10px 14px;
  border-radius: 18px;
  word-break: break-word;
}

.user-bubble {
  background: #0d6efd;
  color: white;
  border-bottom-right-radius: 4px;
}

.bot-bubble {
  background: white;
  border: 1px solid #dee2e6;
  border-bottom-left-radius: 4px;
  color: #333;
}

.quick-questions {
  background: #f8f9fa;
  flex-shrink: 0;
}

.chatbot-input {
  flex-shrink: 0;
  background: white;
}


.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s ease;
}

.slide-up-enter-from,
.slide-up-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
</style>